export class Product {
    id: string;
    numberExtern: string;
    name: string;
    type: string;
    description: string;
    photo: string;
    categoryId: string;
    categoryName: string;
    price: string;
    amount: string;

    constructor() {
        
    }

    public setCategoryId(categoryId: string) {
        this.categoryId = categoryId;
    }

    public setCategoryName(categoryName: string) {
        this.categoryName = categoryName;
    }

    public setAll() {
        this.id = '';
        this.numberExtern = '';
        this.name = '';
        this.type = '';
        this.description = '';
        this.photo = '';
        this.price = '';
    }

}