import { AdvancedImage } from "@cloudinary/react"
import Card from "../common/Card.jsx"

const PublicPersonHighlight = ({ personData, image }) => {
    
    const fullName = personData.firstName + " " + personData.lastName

    return (
        <div className="w-full flex justify-center">
            <Card className="flex flex-col sm:flex-row">
                <div className="flex justify-center">
                    <AdvancedImage cldImg={image} className="max-w-48 max-h-48 rounded border-4 border-white" />
                </div>
                <div className="flex flex-col p-2 gap-1">
                    <div className="font-bold text-2xl text-center sm:text-start">
                        {fullName}
                    </div>
                    <div className="text-sm">
						{personData.summary}	
					</div>	
                </div>
            </Card>
        </div>
    )
}

export default PublicPersonHighlight