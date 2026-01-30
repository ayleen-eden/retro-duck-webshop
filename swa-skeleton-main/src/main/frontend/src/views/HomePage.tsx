/**
 * This code is part of the skeleton project provided for students of the course "Software
 * Architecture" offered by Innsbruck University.
 */
import logo from '../logo.svg';
import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React, {SyntheticEvent, useEffect, useState, useMemo} from "react";
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import {Card} from 'primereact/card';
import {Tag} from 'primereact/tag';
import {Button} from 'primereact/button';
import {Dropdown} from 'primereact/dropdown';
import {MultiSelect} from 'primereact/multiselect';
import {InputText} from 'primereact/inputtext';
import {IconField} from 'primereact/iconfield';
import {InputIcon} from 'primereact/inputicon';
import {ProductDTO} from '../DTO/product.types';
import {useNavigate} from 'react-router-dom';
import styles from "../styles/PixelButton.module.css"

const HomePage: React.FC = () => {
    // --- STATE ---
    const [allProducts, setAllProducts] = useState<ProductDTO[]>([]);
    const [visibleProducts, setVisibleProducts] = useState<ProductDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    // Filter States
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [sortKey, setSortKey] = useState<string>('');
    const [selectedCategories, setSelectedCategories] = useState<string[] | null>(null);
    const [stockFilter, setStockFilter] = useState<string>('all');

    const navigate = useNavigate();

    // --- FILTER OPTIONS ---
    const sortOptions = [
        {label: 'NAME (A-Z)', value: 'name-asc'},
        {label: 'NAME (Z-A)', value: 'name-desc'},
        {label: 'PRICE (LOW TO HIGH)', value: 'price-asc'},
        {label: 'PRICE (HIGH TO LOW)', value: 'price-desc'}
    ];

    const stockOptions = [
        {label: 'ALL PRODUCTS', value: 'all'},
        {label: 'IN STOCK ONLY', value: 'inStock'},
        {label: 'SOLD OUT ONLY', value: 'outOfStock'}
    ];

    const categoryOptions = useMemo(() => {
        const uniqueCats = new Set<string>();
        allProducts.forEach(p => p.categories?.forEach(c => uniqueCats.add(c)));
        return Array.from(uniqueCats).sort().map(c => ({label: c, value: c}));
    }, [allProducts]);


    // --- INITIAL FETCH ---
    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = () => {
        fetch('/api/products/')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then((data: ProductDTO[]) => {
                console.log("Products loaded:", data);
                setAllProducts(data);
                setVisibleProducts(data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching products:", error);
                setLoading(false);
            });
    };

    // --- FILTER LOGIC ---
    useEffect(() => {
        let result = [...allProducts];

        // Filter: Search
        if (searchQuery) {
            const lowerQuery = searchQuery.toLowerCase();
            result = result.filter(p => p.name.toLowerCase().includes(lowerQuery));
        }

        // Filter: Stock
        if (stockFilter === 'inStock') {
            result = result.filter(p => p.stock > 0);
        } else if (stockFilter === 'outOfStock') {
            result = result.filter(p => p.stock <= 0);
        }

        // Filter: Category
        if (selectedCategories && selectedCategories.length > 0) {
            result = result.filter(p =>
                p.categories && p.categories.some(c => selectedCategories.includes(c))
            );
        }

        // Filter: SortBy
        if (sortKey) {
            result.sort((a, b) => {
                const priceA = a.price * (1 - (a.discount || 0));
                const priceB = b.price * (1 - (b.discount || 0));

                switch (sortKey) {
                    case 'name-asc':
                        return a.name.localeCompare(b.name);
                    case 'name-desc':
                        return b.name.localeCompare(a.name);
                    case 'price-asc':
                        return priceA - priceB;
                    case 'price-desc':
                        return priceB - priceA;
                    default:
                        return 0;
                }
            });
        }

        setVisibleProducts(result);
    }, [allProducts, sortKey, selectedCategories, stockFilter, searchQuery]);


    // --- NAVIGATION & RENDERING ---
    const handleProductClick = (productId: number) => {
        navigate(`/products/${productId}`);
    };

    const renderHeader = (product: ProductDTO) => {
        return (
            <div
                style={{
                    height: '200px',
                    overflow: 'hidden',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: '#f9f9f9',
                }}
            >
                <img
                    alt={product.name}
                    src={product.imageUrl || "/images/duck.png"}
                    style={{maxHeight: '100%', maxWidth: '100%', objectFit: 'contain'}}
                    onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
                        (e.target as HTMLImageElement).src = '/images/duck.png';
                    }}
                />
            </div>
        );
    };

    return (
        <div style={{display: 'flex', flexDirection: 'column', minHeight: '100vh'}}>
            <NavbarComponent/>

            <div className="main-content" style={{flex: 1, padding: '2rem', paddingBottom: '7rem'}}>
                <header className="App-header" style={{minHeight: 'auto', marginBottom: '3rem', padding: '2rem'}}>
                    <img src={logo} className="App-logo" alt="logo" style={{height: '150px'}}/>
                    <h1>THE RETRO DUCK</h1>
                    <h3>WELCOME TO - MAYBE NOT THE BEST - BUT THE DUCKI-EST VIDEOGAME SHOP ON THE INTERNET!</h3>
                    <p>We like ducks. And we like videogames. And we like ducks.</p>
                </header>

                {/* --- FILTER BAR --- */}
                <div
                    className="product-card mb-5 p-4 shadow-2 border-round surface-card flex flex-column xl:flex-row gap-4 justify-content-between align-items-start xl:align-items-center filter-bar"
                    style={{marginBottom: 10}}>

                    {/* 1. Searchbar */}
                    <div className="flex flex-column gap-2 filter-bar-element">
                        <IconField iconPosition="left">
                            <InputIcon className="pi pi-search"/>
                            <InputText
                                id="search"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                placeholder="SEARCH PPRODUCT..."
                                className="w-full md:w-20rem"
                            />
                        </IconField>
                    </div>

                    {/* 2. Filter: Category */}
                    <div className="flex flex-column gap-2 filter-bar-element">
                        <MultiSelect
                            id="categories"
                            value={selectedCategories}
                            options={categoryOptions}
                            onChange={(e) => setSelectedCategories(e.value)}
                            optionLabel="label"
                            placeholder="SELECT CATEGORIES"
                            maxSelectedLabels={2}
                            className="pixel-multiselect"
                            display="chip"
                        />
                    </div>

                    {/* 3. Filter: Stock */}
                    <div className="flex flex-column gap-2 filter-bar-element">
                        <Dropdown
                            id="stock"
                            value={stockFilter}
                            options={stockOptions}
                            onChange={(e) => setStockFilter(e.value)}
                            placeholder="AVAILABILITY"
                            className="pixel-dropdown"
                        />
                    </div>

                    {/* 4. Sort */}
                    <div className="flex flex-column gap-2 w-full md:w-auto filter-bar-element">
                        <Dropdown
                            id="sort"
                            value={sortKey}
                            options={sortOptions}
                            onChange={(e) => setSortKey(e.value)}
                            placeholder="DEFAULT"
                            className="pixel-dropdown"
                        />
                    </div>

                    {/* 5. Reset-Filter-Button */}
                    {(sortKey || stockFilter !== 'all' || (selectedCategories && selectedCategories.length > 0) || searchQuery) && (
                        <Button
                            icon="pi pi-times"
                            label="Reset"
                            className={`${styles.btn} ${styles.btn_grey}`}
                            onClick={() => {
                                setSortKey('');
                                setStockFilter('all');
                                setSelectedCategories(null);
                                setSearchQuery('');
                            }}
                            tooltip="CLEAR ALL FILTERS"
                            tooltipOptions={{className: 'pixel-tooltip'}}
                        />
                    )}
                </div>

                {/* --- PRODUCTS GRID --- */}
                {loading ? (
                    <div style={{textAlign: 'center'}}>LOADING PRODUCTS...</div>
                ) : (
                    <>
                        {visibleProducts.length === 0 ? (
                            <div className="text-center p-5 surface-50 border-round">
                                <i className="pi pi-search text-4xl mb-3 text-500"></i>
                                <h3>NO DUCKS FOUND MATCHING YOUR CRITERIA. 🦆</h3>
                                <p>TRY ADJUSTING YOUR SEARCH OR FILTERS.</p>
                            </div>
                        ) : (
                            <div style={{
                                display: 'grid',
                                gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
                                gap: '2rem'
                            }}>
                                {visibleProducts.map((product) => (
                                    <div
                                        key={product.id}
                                        onClick={() => handleProductClick(product.id)}
                                        style={{height: '100%'}}
                                    >
                                        <Card
                                            title={product.name}
                                            header={renderHeader(product)}
                                            className="product-card-interactive"
                                            style={{height: '100%'}}
                                        >
                                            <div style={{
                                                marginBottom: '1rem',
                                                display: 'flex',
                                                gap: '0.5rem',
                                                flexWrap: 'wrap'
                                            }}>
                                                <Tag
                                                    className={product.stock > 0 ? 'pixel-tag pixel-tag-green' : 'pixel-tag pixel-tag-red'}
                                                    value={product.stock > 0 ? 'In stock' : 'Sold out'}
                                                />
                                                {product.discount > 0 && (
                                                    <Tag value={`-${(product.discount * 100).toFixed(0)}% Sale`}
                                                         className="pixel-tag pixel-tag-yellow"/>
                                                )}
                                                {product.categories && product.categories.map(cat => (
                                                    <Tag key={cat} value={cat} className="pixel-tag pixel-tag-blue"/>
                                                ))}
                                            </div>

                                            <p className="m-0" style={{
                                                minHeight: '3rem',
                                                overflow: 'hidden',
                                                textOverflow: 'ellipsis'
                                            }}>
                                                {product.description}
                                            </p>

                                            <div style={{
                                                marginTop: '1.5rem',
                                                display: 'flex',
                                                justifyContent: 'space-between',
                                                alignItems: 'center'
                                            }}>
                                                <div style={{display: 'flex', flexDirection: 'column'}}>
                                                    {product.discount > 0 && (
                                                        <span style={{
                                                            textDecoration: 'line-through',
                                                            fontSize: '0.9rem',
                                                            color: '#999'
                                                        }}>
                                                            {product.price.toFixed(2)} €
                                                        </span>
                                                    )}
                                                    <span style={{
                                                        fontWeight: 'bold',
                                                        fontSize: '1.2rem',
                                                        color: 'var(--primary-color)'
                                                    }}>
                                                        {(product.price * (1 - (product.discount || 0))).toFixed(2)} €
                                                    </span>
                                                </div>
                                            </div>
                                        </Card>
                                    </div>
                                ))}
                            </div>
                        )}
                    </>
                )}
            </div>

            <FooterComponent/>
        </div>
    );
};

export default HomePage;