import Socials from "../../components/modals/Socials.jsx";

const ContactContent = () => {
  return (
    <div className="flex flex-col gap-2">
      <div className="flex gap-2">
        <div>📧 Email:</div>
        <a className="text-rdg-blue hover:underline" href="mailto:info@rdg.org">
          info@rdg.org
        </a>
      </div>
      <div className="w-fit pt-2">
        <Socials />
      </div>
    </div>
  );
};

export default ContactContent;
