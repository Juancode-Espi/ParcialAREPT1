
const index=(function(){
    return{
        city(){
            window.location.replace("consulta?lugar="+document.getElementById("city"));
        }
    }
})();