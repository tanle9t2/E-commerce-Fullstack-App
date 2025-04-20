import styled from "styled-components"
import Section from "../../ui/Section"
import StartRaing from "../../ui/StartRaing"
import ButtonGroup from "../../ui/ButtonGroup"
import Button from "../../ui/Button"
import Comment from "../../ui/Comment"
import Pagination from "../../ui/Pagination"
import { useSearchParams } from "react-router-dom"
import { useComments } from "./useComment"

const OverviewRate = styled.div`
    display:flex;
    background-color:#fffbf8;
    padding:20px;
`
const StatisRate= styled.div`
    font-size:2.5rem;
    color:var(--primary-color);
`
const Number =styled.span`
    font-weight:bold;
`
const GroupSelection = styled.div`
    flex:1;
    padding:20px;
    display:flex;
    height: 15px;
    justify-content:space-around;
`
const AreaComment = styled.ul`
    
`

function ProductRate() {
    const {isLoading,comments: c,count} = useComments();
    if(isLoading) return;
    return (
        <Section padding columns ={1} bgcolor={'#fff'}>
            <OverviewRate>
                <StatisRate>
                    <Number>4.9</Number> trên 5
                    <StartRaing count={5} color="red"/>
                </StatisRate>
                <GroupSelection>
                    <Button >Tất cả</Button>
                    <Button variation ="select">5 Sao</Button>
                    <Button variation ="select">4 Sao</Button>
                    <Button variation ="select">3 Sao</Button>
                    <Button variation ="select">2 Sao</Button>
                    <Button variation ="select">1 Sao</Button>
                </GroupSelection>
            </OverviewRate>
            <AreaComment>
               {c.map(cm => <Comment key={cm.id} comment={cm}/>)}
            </AreaComment>
          <Pagination totalResult={count}/>

        </Section>
    )
}

export default ProductRate
