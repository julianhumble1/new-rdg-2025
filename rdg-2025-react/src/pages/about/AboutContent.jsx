import EditableTextWrapper from "../../components/common/EditableTextWrapper.jsx"

const AboutContent = () => {
  return (
    <EditableTextWrapper contentKey="about-1">
      <div>
        Runnymede Drama Group (charity no. 292974) is run by volunteer committee
        members, elected each year at our AGM, and powered by a membership with
        a collective passion for theatre.
      </div>
      <div>
        Since 1948, we’ve been creating high-quality community theatre and now
        produce at least five shows each year, ranging from full-length plays to
        award-winning entries in local drama festivals.
      </div>
      <div>
        We are proud to welcome both performers and backstage volunteers.
        Whether you want to act, help build sets, join the technical team, or
        support behind the scenes, there is a place for you at RDG. Full
        membership is open to anyone 16+, and our youth group, The Sundowners,
        offers opportunities for younger performers too with their annual
        musical production.
      </div>
      <a
        href="/join"
        className="bg-rdg-red w-fit p-2 rounded-full font-bold px-6 text-white hover:scale-105 transition"
      >
        Join us
      </a>
    </EditableTextWrapper>
  );
};

export default AboutContent;
