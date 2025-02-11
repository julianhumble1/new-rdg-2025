const SuccessMessage = ({ message }) => {
    
    if (message) {
        return (<div className="text-green-500 w-full text-center p-2">{message}</div>)
    }
      
}

export default SuccessMessage