export default function SlotRow({ slot, onBook }) {
  return (
    <div className="slot-row">
      <span>
        {slot.startTime} – {slot.endTime}
      </span>
      <button
        className="book-btn"
        onClick={() => onBook(slot.slotId)}
      >
        Book
      </button>
    </div>
  );
}
