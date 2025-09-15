import { Carousel, Card } from "flowbite-react";
import Socials from "../modals/Socials.jsx";
import HomeProductionSpotLight from "./HomeProductionSpotLight.jsx";
import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";

const Home = () => {
  const [futureProductions, setFutureProductions] = useState([]);

  const fetchFutureProductions = async () => {
    const response = await ProductionService.getFutureProductions();
    setFutureProductions(response.data.productions);
  };

  useEffect(() => {
    fetchFutureProductions();
  }, []);

  return (
    <>
      <div className="flex justify-center min-h-screen">
        <div className=" p-6 w-full 2xl:w-2/3 bg-slate-200 shadow-xl m-0 md:m-3 md:rounded-lg">
          <div className="text-2xl font-bold mb-3 text-sky-900">
            Home of Runnymede Drama Group
          </div>
          <div className="text-lg">
            Welcome to the website of the Runnymede Drama Group. Based in the
            Surrey town of Chertsey, RDG is one of the most lively and
            successful community theatre groups in the area. At least five
            productions are staged each year, including drama festival entries,
            where the group has enjoyed great success.
          </div>
          <div className="font-bold text-xl pl-0 p-3 underline ">
            Upcoming Productions
          </div>
          <div className="flex flex-col lg:flex-row gap-3">
            <div className="flex flex-col lg:w-1/2">
              <div className="flex flex-col md:flex-row lg:flex-col gap-4 ">
                {futureProductions &&
                  futureProductions
                    .slice(0, 2)
                    .map((production, index) => (
                      <HomeProductionSpotLight
                        key={index}
                        production={production}
                      />
                    ))}
              </div>
            </div>
            <div className="lg:w-1/2 flex flex-col justify-center">
              <div className="flex justify-center ">
                <div className="w-96 xl:w-full h-96 lg:h-[500px]">
                  <Carousel className="bg-black rounded" slideInterval={6000}>
                    <img
                      src="/hedda-photos/IMG-20241104-WA0001.jpg"
                      alt="hedda-photo-1"
                      className="h-fit"
                    />
                    <img
                      src="/hedda-photos/IMG-20241104-WA0002.jpg"
                      alt="hedda-photo-2"
                      className="h-fit w-fit"
                    />
                    <img
                      src="/hedda-photos/IMG-20241104-WA0004.jpg"
                      alt="hedda-photo-3"
                    />
                    <img
                      src="https://www.facebook.com/photo/?fbid=4971782469545627&set=pcb.4971788016211739"
                      alt="hedda-photo-3"
                    />
                  </Carousel>
                </div>
              </div>
              <div className="text-center italic text-lg">
                Some photos of the cast from a recent production
              </div>
            </div>
          </div>
          <div className=" mt-4 flex flex-col gap-3">
            <div>
              This site gives information on our current and future productions,
              our past productions and up to date news about the group.
            </div>
            <div>
              Our site is database driven and we are adding to our archive all
              the time (there are currently 488 productions online). We hope to
              have a full (more than 60 years) archive up and running in due
              course, covering all RDG productions from 1948 to the present day.
              Keep coming back to see how far we&apos;ve got!
            </div>
            <div>
              You can get more information about RDG and what we do by
              contacting us, or coming along to one of our events (check the
              calendar for details of when and where). We are always pleased to
              welcome new members - either acting or backstage. Subscriptions
              are £35 per annum for a full member.
            </div>
          </div>

          <div className="flex justify-center">
            <div className="flex flex-col p-8 w-2/3 justify-center">
              <h1 className="font-bold text-xl py-3 text-center text-sky-900">
                Find us on socials:
              </h1>
              <Socials />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
