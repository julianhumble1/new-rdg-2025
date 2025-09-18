import SingleImageSelect from "./SingleImageSelect.jsx";

const HomeImageSelect = () => {
  const imageSelects = [1, 2, 3];

  return (
    <div className="w-full grid md:grid-cols-3 grid-cols-1">
      {imageSelects.map((number, index) => (
        <SingleImageSelect key={index} position={number} />
      ))}
      {/* <SingleImageSelect />
      <SingleImageSelect />
      <SingleImageSelect /> */}
    </div>
  );
};

export default HomeImageSelect;
