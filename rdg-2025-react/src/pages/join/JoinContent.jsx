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
      <div>
        <div className="font-bold text-rdg-red">Fees</div>
        <ul className="list-disc ml-4">
          <li>
            <strong>Full membership:</strong> £35 per year
          </li>
          <li>
            <strong>Students, retired and unwaged:</strong> 50% discount
          </li>
          <li>
            <strong>New members after February:</strong> pay half for the first
            subscription year
          </li>
          <li>
            <strong>Production fees:</strong>
            <ul className="list-disc ml-4">
              <li>£15 for a full-length play</li>
              <li>£20 for a musical</li>
              <li>£10 for a one-act festival play</li>
            </ul>
          </li>
          <li>
            <strong>Social membership:</strong> £5 per year
          </li>
        </ul>
      </div>
      <div>
        <div className="font-bold text-rdg-red">Rehearsals</div>
        <div>
          We usually rehearse two evenings a week, Monday to Thursday,
          7.45pm–10.15pm. Occasional Sunday rehearsals may be scheduled close to
          performance dates.
        </div>
      </div>
      <div>
        <div className="font-bold text-rdg-red">How We Choose Productions</div>
        <div>
          Every summer, we hold an open meeting where members propose plays they
          would like to direct for the following year. After discussion, a
          balanced programme is selected and confirmed by the committee. Members
          can contribute ideas and help shape future seasons.
        </div>
      </div>
      <div>
        <div className="font-bold text-rdg-red">Benefits of Membership</div>
        <div>
          Being part of RDG means joining a welcoming creative community with
          plenty to enjoy:
          <ul className="list-disc ml-4">
            <li>A varied programme of productions throughout the year</li>
            <li>
              Opportunities for actors of all levels, including youth groups
            </li>
            <li>Backstage and technical experience</li>
            <li>Workshops and skill-building activities (when available)</li>
            <li>
              Social events, including a summer barbecue, Christmas party and
              our popular annual quiz
            </li>
            <li>
              A chance to make new friends and be part of a vibrant local arts
              organisation
            </li>
          </ul>
          <div className="mt-2">
            We’re proud to be a friendly, inclusive group where everyone is
            valued.
          </div>
        </div>
      </div>
      <div>
        <div className="font-bold text-rdg-red">Audition Notices</div>
        <div>
          Audition dates and details are circulated by email to members and
          posted on our website’s “Current Productions” page.
          <div className="mt-2">Notices include:</div>
          <ul className="list-disc ml-4">
            <li>Audition dates and times</li>
            <li>Required audition pieces</li>
            <li>Information from the director</li>
            <li>Script access details</li>
          </ul>
          <div className="mt-2">
            Anyone is welcome to audition—members, new joiners and interested
            visitors alike.
          </div>
        </div>
      </div>
    </>
  );
};

export default JoinContent;
