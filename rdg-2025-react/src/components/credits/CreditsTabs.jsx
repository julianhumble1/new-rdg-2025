import { FilmIcon, MusicalNoteIcon, ScaleIcon, UserGroupIcon, TrophyIcon } from "@heroicons/react/16/solid"
import { Tabs } from "flowbite-react"
import CreditsTable from "./CreditsTable.jsx"
import PersonAwardsTable from "../awards/PersonAwardsTable.jsx"
import ProductionAwardsTable from "../awards/ProductionAwardsTable.jsx"

const CreditsTabs = ({ actingCredits, musicianCredits, producerCredits, awards, creditsParent, handleDelete }) => {
    return (
        creditsParent === "person" ?
            <Tabs variant="underline" className="m-3 mb-0">
                {actingCredits.length > 0 &&
                    <Tabs.Item active title="Acting" icon={ScaleIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={actingCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {musicianCredits.length > 0 &&
                    <Tabs.Item active title="Musician" icon={MusicalNoteIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={musicianCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {producerCredits.length > 0 &&
                    <Tabs.Item active title="Producer" icon={FilmIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={producerCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {awards && awards.length > 0 &&
                    <Tabs.Item active title="Awards" icon={TrophyIcon}>
                        <div className="m-2 overflow-auto">
                            <PersonAwardsTable awards={awards} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
            </Tabs>
            :
            <Tabs variant="underline" className="m-3 mb-0">
                {actingCredits.length > 0 &&
                    <Tabs.Item active title="Cast" icon={UserGroupIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={actingCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {musicianCredits.length > 0 &&
                    <Tabs.Item active title="Music" icon={MusicalNoteIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={musicianCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {producerCredits.length > 0 &&
                    <Tabs.Item active title="Production Team" icon={FilmIcon}>
                        <div className="m-2 overflow-auto">
                            <CreditsTable credits={producerCredits} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
                {awards && awards.length > 0 &&
                    <Tabs.Item active title="Awards" icon={TrophyIcon}>
                        <div className="m-2 overflow-auto">
                            <ProductionAwardsTable awards={awards} handleDelete={handleDelete} />
                        </div>
                    </Tabs.Item>
                }
            </Tabs>

    )
}

export default CreditsTabs