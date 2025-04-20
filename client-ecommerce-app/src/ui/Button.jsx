import styled, { css } from "styled-components";

const sizes = {
  small: css`
    font-size: 1.2rem;
    padding: 0.4rem 0.8rem;
    text-transform: uppercase;
    font-weight: 600;
    text-align: center;
  `,
  medium: css`
    font-size: 1.4rem;
    padding: 1.2rem 1.6rem;
    font-weight: 500;
    width:45%;
  `,
  large: css`
    font-size: 1.6rem;
    padding: 1.2rem 2.4rem;
    width:20%;
    font-weight: 500;
  `,
};

const variations = {
  primary: css`
    color: var(--color-brand-50);
    background-color: var(--primary-color);

    &:hover {
      background-color: var(--primary-color);
    }
  `,
  secondary: css`
    color: var(--color-grey-600);
    background: var(--color-grey-0);
    border: 1px solid var(--color-grey-200);

    &:hover {
      background-color: var(--color-grey-50);
    }
  `,
  select: css`
  color: var(--color-grey-600);
  background: transparent;
  border: 1px solid var(--color-grey-200);
  margin:0 10px;

  &:hover {
    border: 1px solid var(--primary-color);
    background-color: var(--color-grey-50);
  }
`,
  danger: css`
    color: var(--color-red-100);
    background-color: var(--color-red-700);

    &:hover {
      background-color: var(--color-red-800);
    }
  `,
};

const Button = styled.button`
  display: flex;
  align-items: center;
  justify-content:center;
  border: ${(props) => props.active !== 0 ? `1px solid var(--primary-color)!important` : `none`};
  border-radius: var(--border-radius-sm);
  box-shadow: var(--shadow-sm);
  ${(props) => sizes[props.size]}
  ${(props) => variations[props.variation]}
`;

Button.defaultProps = {
  variation: "primary",
  size: "medium",
};

export default Button;
