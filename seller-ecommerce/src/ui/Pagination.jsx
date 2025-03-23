import { Pagination as MyPagination } from '@mui/material';
import { useSearchParams } from 'react-router-dom';
import styled from 'styled-components';

const StyledPagination = styled.div`
    display:flex;
    justify-content:center;
    font-size:3rem;
`
function Pagination({ pages }) {
    const [searchParams, setSearchParams] = useSearchParams();
    const currentPage = searchParams.get("page") ? parseInt(searchParams.get("page")) : 1;

    function handleOnChange(e, value) {
        searchParams.set("page", value);
        setSearchParams(searchParams);
    }

    if (pages <= 1) return null;
    return (
        <StyledPagination>
            <MyPagination sx={{
                "& .Mui-selected": {
                    backgroundColor: "red !important", // Dynamic colors
                    color: "white",
                },
                '& .MuiPaginationItem-root': {
                    fontSize: '1.6rem', // Adjust font size here
                }
            }}
                page={currentPage}
                onChange={handleOnChange}
                size="large" count={pages} shape="rounded" />
        </StyledPagination>

    )
}

export default Pagination
