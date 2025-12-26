import JoinAccordion from "./JoinAccordion/JoinAccordion.jsx";

const JoinContent = () => {
  return (
    <>
      <div>
        RDG is always delighted to welcome new members—whether you want to
        perform, help backstage, join our technical team, or support the group
        in other ways. There’s no audition required to become a member, and
        you’ll find a warm, friendly creative community ready to get you
        involved.
      </div>
      <div className="md:px-2">
        <JoinAccordion />
      </div>
      <div>
        <div className="font-bold text-rdg-red">How to Join</div>
        <div>
          You can join at any time — no audition required. Contact our Secretary
          to get started:
          <div className="text-rdg-blue">📧 info@rdg.org</div>
        </div>
      </div>
    </>
  );
};

export default JoinContent;
