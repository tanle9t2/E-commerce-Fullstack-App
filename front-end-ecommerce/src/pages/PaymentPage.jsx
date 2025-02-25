import React, { useState } from "react";
import styled from "styled-components";
import Logo from "../ui/Logo";
import OrderItem from "../features/orders/OrderItem";
import OrderCard from "../features/orders/OderCard"
import { useOrders } from "../features/orders/useOrders";
import Spinner from "../ui/Spinner";
import Checkout from "../features/payment/Checkout";
// Styled Components

const HeaderPayment = styled.div`
    display:flex;
    align-items:end;
    padding: var(--padding-container);
`
const TextLarge = styled.p`
   font-size:2.5rem;
   padding:0 10px;
   margin:0 10px;
   color: var(--primary-color);
   border-left:1px solid var(--primary-color);
   line-height: 33px;
`

function PaymentPage() {
    return (
        <>
            <HeaderPayment>
                <Logo opacity={false} />
                <TextLarge>Thanh toán</TextLarge>
            </HeaderPayment>
            <Checkout/>
        </>
    );
};

export default PaymentPage;
