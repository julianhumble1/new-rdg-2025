import ContentColumn from "./ContentColumn.jsx";
import PhotoColumn from "./PhotoColumn.jsx";

const StandardPageLayout = ({
  photoPosition = "left",
  imgSrc,
  title,
  content,
  extraComponent,
  extraComponentSide = "left",
}) => {
  const replacesPhoto = extraComponent && extraComponentSide === photoPosition;
  const replacesContent = extraComponent && extraComponentSide !== photoPosition;

  return (
    <div
      className={`max-w-[1440px] mx-auto flex flex-1 w-full ${photoPosition === "left" ? "md:flex-row" : "md:flex-row-reverse"} flex-col-reverse pt-5`}
    >
      {replacesPhoto ? extraComponent : <PhotoColumn imgSrc={imgSrc} />}
      {replacesContent ? extraComponent : <ContentColumn title={title} content={content} />}
    </div>
  );
};

export default StandardPageLayout;
