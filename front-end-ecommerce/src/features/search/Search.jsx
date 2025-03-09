import Empty from "../../ui/Empty";
import LayoutWithSideBar from "../../ui/LayoutWithSideBar"
import Spinner from "../../ui/Spinner";
import ProductSearch from "../products/ProductSearch";
import SiderBar from "../products/SiderBar";
import { useGetFilterSearch } from "../products/useGetFilterSearch";
import { useSearchProduct } from "./useSearchProduct";

function Search() {
    const { products, totalPages, isLoading } = useSearchProduct();
    const { filters, isLoading: loadingFilters } = useGetFilterSearch()

    if (isLoading || loadingFilters) return <Spinner />
    if (!products.length) return <Empty resource={"Sản phẩm"} />;
    return (
        <LayoutWithSideBar>
            <SiderBar filters={filters} />
            <ProductSearch products={products} totalPages={totalPages} columns={5} />
        </LayoutWithSideBar>
    )
}

export default Search
