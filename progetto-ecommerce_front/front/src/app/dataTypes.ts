/**
 * Tale interfaccia ci aiuterà a incapsulare i nostri ogg json che viaggiano sul web
 * in modo da poter richiamare determinate proprietà. 
 */

export interface Product{
    id: number,
    name: string,
    type: string,
    model: string,
    code: string,
    price: number,
    quantity: number
}