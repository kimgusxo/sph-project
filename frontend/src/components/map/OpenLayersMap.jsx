import { useEffect, useRef, useState } from "react";
import Map from "ol/Map";
import View from "ol/View";
import Overlay from "ol/Overlay";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import OSM from "ol/source/OSM";
import VectorSource from "ol/source/Vector";
import { fromLonLat } from "ol/proj";
import Style from "ol/style/Style";
import Icon from "ol/style/Icon";
import "./OpenLayersMap.css";

const DEFAULT_CENTER = fromLonLat([126.978, 37.5665]);

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

const ACTIVE_MARKER = buildMarkerSvg("#52c6f2");

function createMarkerStyle() {
  return new Style({
    image: new Icon({
      src: ACTIVE_MARKER,
      imgSize: [64, 82],
      anchor: [0.5, 1],
      scale: 0.95,
    }),
  });
}

function parseFeatureCoordinate(feature) {
  const coordinates = feature?.geometry?.coordinates ?? [];
  const [x, y] = coordinates;
  return { x, y };
}

function parseFeatureProperties(feature) {
  return feature?.properties ?? {};
}

export default function OpenLayersMap({ locations, focusTarget }) {
  const mapRef = useRef(null);
  const mapElementRef = useRef(null);
  const popupRef = useRef(null);
  const overlayRef = useRef(null);
  const locationSourceRef = useRef(null);

  const [selectedFeature, setSelectedFeature] = useState(null);

  useEffect(() => {
    if (!mapElementRef.current || mapRef.current) return;

    const locationSource = new VectorSource();

    const locationLayer = new VectorLayer({
      source: locationSource,
      zIndex: 20,
    });

    const popupOverlay = new Overlay({
      element: popupRef.current,
      positioning: "bottom-center",
      stopEvent: true,
      offset: [0, -18],
      autoPan: {
        animation: {
          duration: 200,
        },
      },
    });

    const map = new Map({
      target: mapElementRef.current,
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
        locationLayer,
      ],
      overlays: [popupOverlay],
      view: new View({
        center: DEFAULT_CENTER,
        zoom: 15,
      }),
    });

    map.on("singleclick", (event) => {
      const feature = map.forEachFeatureAtPixel(event.pixel, (targetFeature) => {
        if (targetFeature.get("featureType") === "location") {
          return targetFeature;
        }
        return null;
      });

      if (!feature) {
        setSelectedFeature(null);
        popupOverlay.setPosition(undefined);
        return;
      }

      const featureData = feature.get("featureData");
      setSelectedFeature(featureData);
      popupOverlay.setPosition(feature.getGeometry().getCoordinates());
    });

    mapRef.current = map;
    overlayRef.current = popupOverlay;
    locationSourceRef.current = locationSource;

    return () => {
      map.setTarget(undefined);
    };
  }, []);

  useEffect(() => {
    if (!locationSourceRef.current) return;

    const source = locationSourceRef.current;
    source.clear();

    const features = locations
      .map((item, index) => {
        const coordinate = parseFeatureCoordinate(item);

        if (coordinate.x == null || coordinate.y == null) {
          return null;
        }

        const feature = new Feature({
          geometry: new Point(fromLonLat([coordinate.x, coordinate.y])),
        });

        feature.setId(parseFeatureProperties(item).systemId ?? index);
        feature.set("featureType", "location");
        feature.set("featureData", item);
        feature.setStyle(createMarkerStyle());

        return feature;
      })
      .filter(Boolean);

    source.addFeatures(features);
  }, [locations]);

  useEffect(() => {
    if (!mapRef.current || !focusTarget || focusTarget.x == null || focusTarget.y == null) {
      return;
    }

    const center = fromLonLat([focusTarget.x, focusTarget.y]);

    mapRef.current.getView().animate({
      center,
      zoom: 16,
      duration: 300,
    });
  }, [focusTarget]);

  const handleClosePopup = () => {
    setSelectedFeature(null);
    overlayRef.current?.setPosition(undefined);
  };

  const selectedProps = selectedFeature ? parseFeatureProperties(selectedFeature) : null;
  const selectedCoord = selectedFeature ? parseFeatureCoordinate(selectedFeature) : null;

  return (
    <section className="map-panel">
      <div className="map-toolbar">
        <div className="map-toolbar__chip">
          <span className="material-symbols-rounded">place</span>
          <span>조회 위치 {locations.length}개</span>
        </div>
      </div>

      <div ref={mapElementRef} className="ol-map-root" />

      <div
        ref={popupRef}
        className={`map-popup ${selectedFeature ? "is-open" : ""}`}
      >
        {selectedFeature && (
          <>
            <button
              type="button"
              className="map-popup__close"
              onClick={handleClosePopup}
            >
              <span className="material-symbols-rounded">close</span>
            </button>

            <div className="map-popup__title">측정 위치</div>

            {selectedProps?.systemId != null && (
              <div className="map-popup__meta">
                시스템 ID: {selectedProps.systemId}
              </div>
            )}

            {selectedProps?.address && (
              <div className="map-popup__meta">
                주소: {selectedProps.address}
              </div>
            )}

            {selectedProps?.lightSourceCount != null && (
              <div className="map-popup__meta">
                광원 개수: {selectedProps.lightSourceCount}
              </div>
            )}

            {selectedCoord && (
              <div className="map-popup__meta">
                좌표: {selectedCoord.x}, {selectedCoord.y}
              </div>
            )}
          </>
        )}
      </div>
    </section>
  );
}