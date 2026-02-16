import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

export default function RightDrawer({ open, onClose, title, children }) {
  const [mounted, setMounted] = useState(false);
  const [animateIn, setAnimateIn] = useState(false);

  // ESC key close
  useEffect(() => {
    if (!open) return;

    const handleEsc = (e) => {
      if (e.key === "Escape") onClose();
    };

    window.addEventListener("keydown", handleEsc);
    return () => window.removeEventListener("keydown", handleEsc);
  }, [open, onClose]);

  // Mount + animation handling
    useEffect(() => {
        const onEsc = (e) => {
            if (e.key === "Escape") onClose();
          };
        if (open) {
            
            setMounted(true);
            window.addEventListener("keydown", onEsc);

      requestAnimationFrame(() => {
        requestAnimationFrame(() => {
          setAnimateIn(true);
        });
      });
    } else {
      setAnimateIn(false);

      const timer = setTimeout(() => {
        setMounted(false);
      }, 300);

            return () => {
                window.removeEventListener("keydown", onEsc);  
                clearTimeout(timer);
            }
    }
  }, [open, onClose]);

  if (!mounted) return null;

  return createPortal(
    <>
      {/* BACKDROP */}
      <div
        onClick={onClose}
        className={`
          fixed inset-0 z-40
          backdrop-blur-sm bg-black/20
          transition-opacity duration-300
          ${animateIn ? "opacity-100" : "opacity-0"}
        `}
      />

      {/* DRAWER */}
      <div
        className={`
          fixed top-0 right-0 z-50
          h-full bg-white shadow-2xl
          transform transition-transform duration-300 ease-in-out
          w-full sm:w-[420px]
          ${animateIn ? "translate-x-0" : "translate-x-full"}
        `}
      >
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b">
          <h2 className="text-lg font-semibold">{title}</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-black text-xl"
          >
            ×
          </button>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto h-[calc(100%-64px)]">
          {children}
        </div>
      </div>
    </>,
    document.body
  );
}
