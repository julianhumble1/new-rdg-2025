import StandardPageLayout from "../../components/common/PageLayout/StandardPageLayout.jsx";
import AboutContent from "./AboutContent.jsx";

const About = () => {
  return (
    <StandardPageLayout
      photoPosition="right"
      imgSrc="/images/scribble/2.svg"
      title="Who We Are"
      content={AboutContent}
    />
  );
};

export default About;
