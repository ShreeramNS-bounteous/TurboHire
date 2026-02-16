export default function FiltersPanel({ onSearch, filters }) {
    return (
      <div className="filters">
        <div className="content-header">
          <h4>Filters</h4>
        </div>
  
        <div className="filter-div">
          <label>Date</label>
          <input
            type="date"
            value={filters.date}
            onChange={(e) =>
              onSearch({ ...filters, date: e.target.value })
            }
          />
  
          <label>From</label>
          <input
            type="time"
            value={filters.from}
            onChange={(e) =>
              onSearch({ ...filters, from: e.target.value })
            }
          />
  
          <label>To</label>
          <input
            type="time"
            value={filters.to}
            onChange={(e) =>
              onSearch({ ...filters, to: e.target.value })
            }
          />
  
          <button
            className="search-btn"
            onClick={() => onSearch(filters)}
          >
            Search
          </button>
        </div>
      </div>
    );
  }
  