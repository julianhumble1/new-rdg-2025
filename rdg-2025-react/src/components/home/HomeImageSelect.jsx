import SingleImageSelect from "./SingleImageSelect.jsx";
import ContentCard from "../common/ContentCard.jsx";

const HomeImageSelect = () => {
  const imageSelects = [1, 2, 3];

  return (
    <ContentCard>
      <div className="text-2xl font-bold">Home Image Select</div>
      <div>
        Here you can change the images that will show on the home screen photo
        carousel. Landscape pictures will be formatted better than portrait.
      </div>
      <div className="w-full grid md:grid-cols-3 grid-cols-1">
        {imageSelects.map((number, index) => (
          <SingleImageSelect key={index} position={number} />
        ))}
      </div>
    </ContentCard>
  );
};

export default HomeImageSelect;
