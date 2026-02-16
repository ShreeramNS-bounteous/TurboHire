import { useEffect, useState } from "react";
import api from "../../api/axios";
import RightDrawer from "../../components/RightDrawer";
import CandidateRow from "./CandidateRow";
import CandidateProfileDrawer from "./CandidateProfileDrawer";

const PAGE_SIZE = 5;

export default function Candidates() {
  const [candidates, setCandidates] = useState([]);
  const [profiles, setProfiles] = useState({});
  const [page, setPage] = useState(1);

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [selectedProfile, setSelectedProfile] = useState(null);
  const [resume, setResume] = useState(null);
  const [loadingDrawer, setLoadingDrawer] = useState(false);

  useEffect(() => {
    api.get("/api/candidates").then((res) => {
      setCandidates(res.data || []);
    });
  }, []);

  const paginated = candidates.slice(
    (page - 1) * PAGE_SIZE,
    page * PAGE_SIZE
  );

  // Load profile summaries for visible rows
  useEffect(() => {
    paginated.forEach((c) => {
      if (!profiles[c.id]) {
        api.get(`/api/candidates/${c.id}/profile`).then((res) => {
          setProfiles((prev) => ({
            ...prev,
            [c.id]: res.data,
          }));
        });
      }
    });
  }, [paginated]);

  const openProfile = async (candidate) => {
    setDrawerOpen(true);
    setSelectedCandidate(candidate);
    setLoadingDrawer(true);
    setSelectedProfile(null);
    setResume(null);

    try {
      const [profileRes, resumeRes] = await Promise.all([
        api.get(`/api/candidates/${candidate.id}/profile`),
        api.get(`/api/candidates/${candidate.id}/resume`),
      ]);

      setSelectedProfile(profileRes.data);
      setResume(resumeRes.data);
    } finally {
      setLoadingDrawer(false);
    }
  };

  const totalPages = Math.ceil(candidates.length / PAGE_SIZE);

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-6">Candidates</h1>

      <div className="space-y-4">
        {paginated.map((c) => (
          <CandidateRow
            key={c.id}
            candidate={c}
            profile={profiles[c.id]}
            onClick={() => openProfile(c)}
          />
        ))}
      </div>

      {/* Pagination */}
      <div className="flex justify-between items-center mt-6">
        <button
          disabled={page === 1}
          onClick={() => setPage(page - 1)}
          className="border px-3 py-1 rounded disabled:opacity-50"
        >
          Prev
        </button>

        <span className="text-sm text-gray-500">
          Page {page} of {totalPages}
        </span>

        <button
          disabled={page === totalPages}
          onClick={() => setPage(page + 1)}
          className="border px-3 py-1 rounded disabled:opacity-50"
        >
          Next
        </button>
      </div>

      <RightDrawer
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        title="Candidate Profile"
      >
        <CandidateProfileDrawer
          loading={loadingDrawer}
          candidate={selectedCandidate}
          profile={selectedProfile}
          resume={resume}
        />
      </RightDrawer>
    </div>
  );
}
