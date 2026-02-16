import SlotRow from "./SlotRow";

export default function InterviewerCard({ interviewer,onBook }) {
  return (
    <div className="interviewer-card">
      <div className="card-header">
        <div className="avatar">👤</div>
        {console.log(interviewer)}

        <div>
          <h4>{interviewer.userName}</h4>
          <p>
            {interviewer.expertise} · {interviewer.experienceYears} yrs
          </p>
        </div>

        <button className="info-btn">More info</button>
      </div>

      <div className="slots">
        {interviewer.slots.map((slot) => (
          <SlotRow key={slot.slotId} slot={slot} onBook={onBook}/>
        ))}
      </div>
    </div>
  );
}
