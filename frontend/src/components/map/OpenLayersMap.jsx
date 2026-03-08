import { useEffect, useMemo, useRef, useState } from "react";
import Map from "ol/Map";
import View from "ol/View";
import Overlay from "ol/Overlay";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import CircleGeometry from "ol/geom/Circle";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import OSM from "ol/source/OSM";
import VectorSource from "ol/source/Vector";
import { fromLonLat } from "ol/proj";
import Style from "ol/style/Style";
import Fill from "ol/style/Fill";
import Stroke from "ol/style/Stroke";
import CircleStyle from "ol/style/Circle";
import Icon from "ol/style/Icon";
import { haversineDistanceMeters } from "../../utils/geo";
import "./OpenLayersMap.css";

const DEFAULT_CENTER = fromLonLat([126.978, 37.5665]);

const ACTIVE_MARKER_SRC = buildMarkerSvg("#52c6f2");
const DIMMED_MARKER_SRC = buildMarkerSvg("#667388");

function buildMarkerSvg(fillColor) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="64" height="82" viewBox="0 0 64 82">
      <path d="M32 2C18.745 2 8 12.745 8 26c0 18.6 24 50 24 50s24-31.4 24-50C56 12.745 45.255 2 32 2z" fill="${fillColor}" />
      <circle cx="32" cy="26" r="13" fill="rgba(255,255,255,0.14)" />
      <path d="M32 16.5l3.6 7.2 8 1.1-5.8 5.7 1.4 8-7.2-3.8-7.2 3.8 1.4-8-5.8-5.7 8-1.1z" fill="#ffffff" />
    </svg>
  `;

  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}

function createLocationStyle(isDimmed) {
  return new Style({
    image: new Icon({
      src: isDimmed ? DIMMED_MARKER_SRC : ACTIVE_MARKER_SRC,
      imgSize: [64, 82],
      anchor: [0.5, 1],
      scale: isDimmed ? 0.86 : 0.94,
    }),
  });
}

function createRadiusCircleStyle() {
  return new Style({
    fill: new Fill({
      color: "rgba(91, 194, 237, 0.18)",
    }),
    stroke: new Stroke({
      color: "#5bc2ed",
      width: 2,
      lineDash: [8, 6],
    }),
  });
}

function createRadiusCenterStyle() {
  return new Style({
    image: new CircleStyle({
      radius: 6,
      fill: new Fill({
        color: "#f4fbff",
      }),
      stroke: new Stroke({
        color: "#44b6e8",
        width: 3,
      }),
    }),
  });
}

export default function OpenLayersMap({ locations, radiusQuery, focusTarget }) {
  const mapElementRef = useRef(null);
  const popupElementRef = useRef(null);

  const mapRef = useRef(null);
  const popupOverlayRef = useRef(null);
  const locationSourceRef = useRef(null);
  const radiusSourceRef = useRef(null);

  const [selectedLocation, setSelectedLocation] = useState(null);

  const radiusInfoText = useMemo(() => {
    if (!radiusQuery) return null;
    return `${radiusQuery.label} / 반경 ${Math.round(
      radiusQuery.radiusMeters / 1000
    )}km`;
  }, [radiusQuery]);

  useEffect(() => {
    if (!mapElementRef.current || mapRef.current) return;

    const locationSource = new VectorSource();
    const radiusSource = new VectorSource();

    const locationLayer = new VectorLayer({
      source: locationSource,
      zIndex: 20,
    });

    const radiusLayer = new VectorLayer({
      source: radiusSource,
      zIndex: 10,
    });

    const popupOverlay = new Overlay({
      element: popupElementRef.current,
      autoPan: {
        animation: {
          duration: 220,
        },
      },
      positioning: "bottom-center",
      stopEvent: true,
      offset: [0, -18],
    });

    const map = new Map({
      target: mapElementRef.current,
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
        radiusLayer,
        locationLayer,
      ],
      overlays: [popupOverlay],
      view: new View({
        center: DEFAULT_CENTER,
        zoom: 15,
      }),
    });

    const handleMapClick = (event) => {
      const feature = map.forEachFeatureAtPixel(event.pixel, (targetFeature) => {
        if (targetFeature.get("featureType") === "location") {
          return targetFeature;
        }
        return null;
      });

      if (!feature) {
        setSelectedLocation(null);
        popupOverlay.setPosition(undefined);
        return;
      }

      const locationData = feature.get("locationData");
      setSelectedLocation(locationData);
      popupOverlay.setPosition(feature.getGeometry().getCoordinates());
    };

    map.on("singleclick", handleMapClick);

    mapRef.current = map;
    popupOverlayRef.current = popupOverlay;
    locationSourceRef.current = locationSource;
    radiusSourceRef.current = radiusSource;

    return () => {
      map.un("singleclick", handleMapClick);
      map.setTarget(undefined);
      mapRef.current = null;
    };
  }, []);

  useEffect(() => {
    if (!locationSourceRef.current) return;

    const source = locationSourceRef.current;
    source.clear();

    const features = locations.map((location) => {
      const coordinate = fromLonLat([location.lon, location.lat]);

      const isDimmed =
        radiusQuery &&
        haversineDistanceMeters(
          radiusQuery.lat,
          radiusQuery.lon,
          location.lat,
          location.lon
        ) > radiusQuery.radiusMeters;

      const feature = new Feature({
        geometry: new Point(coordinate),
      });

      feature.setId(location.id);
      feature.set("featureType", "location");
      feature.set("locationData", location);
      feature.setStyle(createLocationStyle(Boolean(isDimmed)));

      return feature;
    });

    source.addFeatures(features);

    if (selectedLocation) {
      const latest = locations.find((item) => item.id === selectedLocation.id);

      if (!latest) {
        setSelectedLocation(null);
        popupOverlayRef.current?.setPosition(undefined);
      } else {
        setSelectedLocation(latest);
      }
    }
  }, [locations, radiusQuery, selectedLocation]);

  useEffect(() => {
    if (!radiusSourceRef.current) return;

    const source = radiusSourceRef.current;
    source.clear();

    if (!radiusQuery) return;

    const center = fromLonLat([radiusQuery.lon, radiusQuery.lat]);

    const circleFeature = new Feature({
      geometry: new CircleGeometry(center, radiusQuery.radiusMeters),
    });
    circleFeature.setStyle(createRadiusCircleStyle());

    const centerFeature = new Feature({
      geometry: new Point(center),
    });
    centerFeature.setStyle(createRadiusCenterStyle());

    source.addFeatures([circleFeature, centerFeature]);
  }, [radiusQuery]);

  useEffect(() => {
    if (!mapRef.current || !focusTarget) return;

    const center = fromLonLat([focusTarget.lon, focusTarget.lat]);

    mapRef.current.getView().animate({
      center,
      zoom: 16,
      duration: 280,
    });

    if (focusTarget.locationId) {
      const targetLocation = locations.find(
        (item) => item.id === focusTarget.locationId
      );

      if (targetLocation) {
        setSelectedLocation(targetLocation);
        popupOverlayRef.current?.setPosition(center);
      }
    }
  }, [focusTarget, locations]);

  const handleClosePopup = () => {
    setSelectedLocation(null);
    popupOverlayRef.current?.setPosition(undefined);
  };

  return (
    <section className="map-panel">
      <div className="map-toolbar">
        <div className="map-toolbar__chip">
          <span className="material-symbols-rounded">place</span>
          <span>위치 {locations.length}개</span>
        </div>

        {radiusInfoText && (
          <div className="map-toolbar__chip is-accent">
            <span className="material-symbols-rounded">radar</span>
            <span>{radiusInfoText}</span>
          </div>
        )}
      </div>

      <div ref={mapElementRef} className="ol-map-root" />

      <div
        ref={popupElementRef}
        className={`map-popup ${selectedLocation ? "is-open" : ""}`}
      >
        {selectedLocation && (
          <>
            <button
              type="button"
              className="map-popup__close"
              onClick={handleClosePopup}
            >
              <span className="material-symbols-rounded">close</span>
            </button>

            <div className="map-popup__title">{selectedLocation.name}</div>
            <div className="map-popup__meta">ID: {selectedLocation.id}</div>
            <div className="map-popup__meta">{selectedLocation.address}</div>
            <div className="map-popup__meta">
              좌표: {selectedLocation.lon}, {selectedLocation.lat}
            </div>

            {selectedLocation.measurement ? (
              <div className="map-popup__stats">
                <div>max: {selectedLocation.measurement.maxVal}</div>
                <div>min: {selectedLocation.measurement.minVal}</div>
                <div>avg: {selectedLocation.measurement.avgVal}</div>
              </div>
            ) : (
              <div className="map-popup__empty">측정값이 아직 등록되지 않았습니다.</div>
            )}
          </>
        )}
      </div>
    </section>
  );
}