import Socials from "../../components/modals/Socials.jsx";

const ContactContent = () => {
  return (
    <div className="flex flex-col gap-2">
      <div className="flex gap-2">
        <div>📧 Email:</div>
        <div className="text-rdg-blue"> info@rdg.org</div>
      </div>
      <div className="w-fit pt-2">
        <Socials />
      </div>
    </div>
  );
};

export default ContactContent;
