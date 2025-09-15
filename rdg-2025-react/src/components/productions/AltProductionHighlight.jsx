import ProductionImageWithUploadBox from "../photo_components/ProductionImageWithUploadBox.jsx";

const AltProductionHighlight = ({
  productionData,
  setEditMode,
  handleDelete,
  image,
  fetchProductionData,
}) => {
  return (
    <div className="col-span-3 flex justify-center bg-slate-200">
      <div className="flex w-full">
        <ProductionImageWithUploadBox
          productionData={productionData}
          image={image}
          fetchProductionData={fetchProductionData}
        />

        <div className="text-black text-xl font-bold flex justify-between w-full h-fit p-2 sm:flew-row flex-col">
          <div>{productionData.name}</div>
          <div className="flex gap-2 justify-center">
            <button
              className="text-sm hover:underline"
              onClick={() => setEditMode(true)}
            >
              Edit
            </button>
            <button
              className="text-sm hover:underline"
              onClick={() => handleDelete(productionData)}
            >
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AltProductionHighlight;
