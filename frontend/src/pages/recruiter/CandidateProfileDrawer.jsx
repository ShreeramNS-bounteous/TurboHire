export default function CandidateProfileDrawer({
  loading,
  candidate,
  profile,
  resume,
}) {
  // ⛔ HARD STOP — nothing renders until profile exists
  if (loading || !candidate || !profile) {
    return (
      <p className="text-gray-500 text-sm">
        Loading profile...
      </p>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h2 className="text-xl font-semibold">
          {candidate.fullName}
        </h2>
        <p className="text-sm text-gray-500">
          {candidate.email}
        </p>
      </div>

      {/* Education */}
      <Section  title="Education">
        {profile.education ? (
          <p>
            {profile.education.degree} —{" "}
            {profile.education.college} (
            {profile.education.year})
          </p>
        ) : (
          "—"
        )}
      </Section>

      {/* Experience */}
      <Section title="Experience">
        {profile.totalExperience != null
          ? `${profile.totalExperience} years`
          : "—"}
      </Section>

      {/* Skills */}
      <Section title="Skills">
      <div className="flex flex-wrap gap-2">
        {profile.skills?.length
          ? profile.skills.map((s, i) => ( <span key={i} className="bg-blue-100 text-blue-700 px-2 py-1 rounded text-sm" > {s} </span>))
            : "—"}
      </div>
      </Section>

      {/* Resume */}
      <Section title="Resume">
        {resume?.resumePdf ? (
          <iframe
            title="Resume"
            className="w-full h-96 border rounded"
            src={`data:application/pdf;base64,${resume.resumePdf}`}
          />
        ) : (
          "No resume uploaded"
        )}
      </Section>
    </div>
  );
}

function Section({ title, children }) {
  return (
    <div>
      <h3 className="font-medium mb-1">
        {title}
      </h3>
      <div className="text-sm text-gray-700">
        {children}
      </div>
    </div>
  );
}
