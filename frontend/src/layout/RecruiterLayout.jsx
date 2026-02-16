// src/layout/RecruiterLayout.jsx
import Navbar from "./Navbar";
import { Outlet } from "react-router-dom";

export default function RecruiterLayout() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <main className="max-w-7xl mx-auto px-6 py-6">
        <Outlet />
      </main>
    </div>
  );
}
