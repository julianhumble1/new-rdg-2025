const MembershipContent = () => {
  return (
    <div className="flex flex-col gap-2">
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
    </div>
  );
};

export default MembershipContent;
