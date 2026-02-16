import { useState } from "react";

export default function BookSlotModal({
  isOpen,
  onClose,
  onConfirm,
  slotId
}) {
  const [meetingUrl, setMeetingUrl] = useState("");

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h3>Add Meeting Link</h3>

        <input
          type="text"
          placeholder="Enter Teams / Meet URL"
          value={meetingUrl}
          onChange={(e) => setMeetingUrl(e.target.value)}
        />

        <div className="modal-actions">
          <button onClick={onClose} className="cancel-btn">
            Cancel
          </button>

          <button
            onClick={() => onConfirm(slotId, meetingUrl)}
            className="confirm-btn"
            disabled={!meetingUrl}
          >
            Confirm Booking
          </button>
        </div>
      </div>
    </div>
  );
}
