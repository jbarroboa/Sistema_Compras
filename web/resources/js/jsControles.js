/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//ya no se esta utilizando este metodo
function limpiarFormulario(idFormulario)
{
    document.getElementById(idFormulario).reset();
}
function justNumbers(e)
{
    var keynum = window.event ? window.event.keyCode : e.which;
    if ((keynum == 8) )
        return true;

    return /\d/.test(String.fromCharCode(keynum));
}


