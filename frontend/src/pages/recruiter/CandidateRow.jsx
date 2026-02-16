export default function CandidateRow({ candidate, profile, onClick }) {
    return (
      <div
        onClick={onClick}
        className="bg-white border rounded p-5 hover:shadow-md cursor-pointer transition"
      >
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-full bg-orange-500 text-white flex items-center justify-center text-lg font-semibold">
            {candidate.fullName?.[0]}
          </div>
  
          <div>
            <p className="font-semibold text-lg">{candidate.fullName}</p>
            <p className="text-sm text-gray-500">{candidate.email}</p>
          </div>
        </div>
  
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
          <SummaryCard title="Education">
            {profile?.education
              ? `${profile.education.degree} · ${profile.education.college}`
              : "—"}
          </SummaryCard>
  
          <SummaryCard title="Experience">
            {profile?.totalExperience
              ? `${profile.totalExperience} years`
              : "—"}
          </SummaryCard>
  
          <SummaryCard title="Skills">
            {profile?.skills?.length
              ? profile.skills.slice(0, 3).join(", ")
              : "—"}
          </SummaryCard>
        </div>
      </div>
    );
  }
  
  function SummaryCard({ title, children }) {
    return (
      <div className="border rounded p-3">
        <p className="text-sm font-medium">{title}</p>
        <p className="text-sm text-gray-600 mt-1">{children}</p>
      </div>
    );
  }
  