const SuccessMessage = ({ message }) => {
    
    if (message) {
        return (<div className="text-green-500">{message}</div>)
    }
      
}

export default SuccessMessage