import {
  Accordion,
  AccordionContent,
  AccordionPanel,
  AccordionTitle,
} from "flowbite-react";
import { joinAccordionContent } from "./content.jsx";

const JoinAccordion = () => {
  return (
    <Accordion>
      {joinAccordionContent.map((item, index) => (
        <AccordionPanel key={index}>
          <>
            <AccordionTitle className="text-rdg-blue font-bold">
              {item.title}
            </AccordionTitle>
            <AccordionContent className="bg-gray-200">
              {item.content}
            </AccordionContent>
          </>
        </AccordionPanel>
      ))}
    </Accordion>
  );
};

export default JoinAccordion;
