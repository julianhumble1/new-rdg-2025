import StandardPageLayout from "../../components/common/PageLayout/StandardPageLayout.jsx";
import ContactContent from "./ContactContent.jsx";

const Contact = () => {
  return (
    <StandardPageLayout
      imgSrc="/images/scribble/10.svg"
      title="Contact Us"
      content={ContactContent}
    />
  );
};

export default Contact;
