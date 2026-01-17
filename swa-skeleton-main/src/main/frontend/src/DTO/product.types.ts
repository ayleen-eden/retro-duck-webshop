export enum ProductCategories {
    AMIGA = `AMIGA`,
    ATARI_2600 = `ATARI_2600`,
    ATARI_5200 = `ATARI_5200`,
    ATARI_7800 = `ATARI_7800`,
    ATARI_JAGUAR = `ATARI_JAGUAR`,
    ATARI_LYNX = `ATARI_LYNX`,
    BILDSCHIRMSPIEL_01 = `BILDSCHIRMSPIEL_01`,
    BSS_01 = `BSS_01`,
    C64 = `C64`,
    COLECOVISION = `COLECOVISION`,
    DREAMCAST = `DREAMCAST`,
    DS = `DS`,
    GAME_GEAR = `GAME_GEAR`,
    GAMECUBE = `GAMECUBE`,
    GB = `GB`,
    GBA = `GBA`,
    GBC = `GBC`,
    GCN = `GCN`,
    GENESIS = `GENESIS`,
    INTELLIVISION = `INTELLIVISION`,
    MASTER_SYSTEM = `MASTER_SYSTEM`,
    N64 = `N64`,
    NEO_GEO = `NEO_GEO`,
    NES = `NES`,
    NINTENDO_3DS = `NINTENDO_3DS`,
    PC = `PC`,
    PLAYSTATION_1 = `PLAYSTATION_1`,
    PS2 = `PS2`,
    PS3 = `PS3`,
    PS4 = `PS4`,
    PS5 = `PS5`,
    PSP = `PSP`,
    PS_VITA = `PS_VITA`,
    SATURN = `SATURN`,
    SNES = `SNES`,
    STEAM_DECK = `STEAM_DECK`,
    SWITCH = `SWITCH`,
    SWITCH_2 = `SWITCH_2`,
    TURBOGRAFX_16 = `TURBOGRAFX_16`,
    VIRTUAL_BOY = `VIRTUAL_BOY`,
    WII = `WII`,
    WII_U = `WII_U`,
    WONDERSWAN = `WONDERSWAN`,
    XBOX = `XBOX`,
    XBOX_360 = `XBOX_360`,
    XBOX_ONE = `XBOX_ONE`,
    XBOX_SERIES = `XBOX_SERIES`
}

export interface ProductDTO {
    id: number;
    name: String;
    description: string;
    price: number;
    stock: number;
    discount: number;
    imageUrl: String;
    categories: ProductCategories[];
}

export interface ProductCreateDTO {
    name: String,
    description: String,
    price: number,
    stock: number,
    discount: number,
    imageUrl: String,
    categories: ProductCategories[]
}
