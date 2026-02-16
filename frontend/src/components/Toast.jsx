export default function Toast({ message, type = "success", onClose }) {
    return (
      <div
        className={`
          fixed bottom-6 right-6 z-[100]
          px-4 py-3 rounded shadow-lg text-white
          transition-all duration-300
          ${type === "success" ? "bg-green-600" : "bg-red-600"}
        `}
      >
        <div className="flex items-center gap-4">
          <span>{message}</span>
          <button
            onClick={onClose}
            className="text-white font-bold"
          >
            ×
          </button>
        </div>
      </div>
    );
  }
  