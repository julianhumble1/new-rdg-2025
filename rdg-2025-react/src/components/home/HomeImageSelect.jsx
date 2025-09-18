import SingleImageSelect from "./SingleImageSelect.jsx";

const HomeImageSelect = () => {
  return (
    <div className="w-full grid md:grid-cols-3 grid-cols-1">
      <SingleImageSelect />
      <SingleImageSelect />
      <SingleImageSelect />
    </div>
  );
};

export default HomeImageSelect;
