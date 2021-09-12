import React, { useState } from "react";

const SearchBar = ({ labelText, onFormSubmit }) => {
  const [term, setTerm] = useState("");

  const onSubmit = (e) => {
    e.preventDefault();
    onFormSubmit(term);
    setTerm("");
  };

  return (
    <div className="search-bar ui segment">
      <form className="ui form" onSubmit={onSubmit}>
        <div className="field">
          <label>{labelText}</label>
          <input
            type="text"
            value={term}
            onChange={(e) => setTerm(e.target.value)}
          />
        </div>
        <button
          type="submit"
          onClick={onSubmit}
          className="fluid grey ui button"
        >
          Search
        </button>
      </form>
    </div>
  );
};

export default SearchBar;
