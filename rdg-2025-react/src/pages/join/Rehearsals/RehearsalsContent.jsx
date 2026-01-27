const RehearsalsContent = () => {
  return (
    <div className="flex flex-col gap-2">
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
        <div className="font-bold text-rdg-red">Rehearsals</div>
        <div>
          We usually rehearse two evenings a week, Monday to Thursday,
          7.45pm–10.15pm. Occasional Sunday rehearsals may be scheduled close to
          performance dates.
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
    </div>
  );
};

export default RehearsalsContent;
