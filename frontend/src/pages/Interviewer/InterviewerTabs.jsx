import { useRef, useEffect, useState } from "react";
import "./interviewer.css";

export default function InterviewerTabs({
  activeTab,
  onChange,
  counts = {},
}) {
  const tabs = [
    { key: "AVAILABILITY", label: "Availability" },
    { key: "SCHEDULED", label: "Scheduled" },
    { key: "COMPLETED", label: "Completed" },
  ];

  const containerRef = useRef(null);
  const [sliderStyle, setSliderStyle] = useState({});

  useEffect(() => {
    const activeButton = containerRef.current?.querySelector(
      `.interviewer-tab[data-tab="${activeTab}"]`
    );

    if (activeButton) {
      setSliderStyle({
        width: activeButton.offsetWidth,
        left: activeButton.offsetLeft,
      });
    }
  }, [activeTab]);

  return (
    <div className="interviewer-tabs-wrapper">
      <div className="interviewer-tabs" ref={containerRef}>
        
        {/* 🔥 Animated Slider */}
        <div
          className="tab-slider"
          style={sliderStyle}
        />

        {tabs.map((tab) => (
          <button
            key={tab.key}
            data-tab={tab.key}
            onClick={() => onChange(tab.key)}
            className={`interviewer-tab ${
              activeTab === tab.key ? "active" : ""
            }`}
          >
            {tab.label}

            {/* 🔥 Badge */}
            {counts[tab.key] > 0 && (
              <span className="tab-badge">
                {counts[tab.key]}
              </span>
            )}
          </button>
        ))}
      </div>
    </div>
  );
}
