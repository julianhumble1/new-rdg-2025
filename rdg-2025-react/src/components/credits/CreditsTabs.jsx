import { FilmIcon, MusicalNoteIcon, ScaleIcon } from "@heroicons/react/16/solid"
import { Tabs } from "flowbite-react"
import CreditsTable from "./CreditsTable.jsx"

const CreditsTabs = ({actingCredits, musicianCredits, producerCredits}) => {
    return (
        <Tabs variant="underline" className="m-3 mb-0">
            {actingCredits.length > 0 && 
                <Tabs.Item active title="Acting" icon={ScaleIcon}>
                    <div className="m-2 overflow-auto">
                        <CreditsTable credits={actingCredits} />
                    </div>
                </Tabs.Item>
            }
            {musicianCredits.length > 0 &&
                <Tabs.Item active title="Musician" icon={MusicalNoteIcon}>
                    <div className="m-2 overflow-auto">
                        <CreditsTable credits={musicianCredits} />
                    </div>
                </Tabs.Item>
            }
            {producerCredits.length > 0 &&
                <Tabs.Item active title="Producer" icon={FilmIcon}>
                    <div className="m-2 overflow-auto">
                        <CreditsTable credits={producerCredits} />
                    </div>
                </Tabs.Item>
            }
        </Tabs>
    )
}

export default CreditsTabs