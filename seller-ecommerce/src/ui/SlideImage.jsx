import { useState } from "react";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";
import styled from "styled-components";

const SliderContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 80%;
    margin: auto;
`;

const MainImageContainer = styled.div`
    width: 80%;
    margin-bottom: 10px;
`;

const MainImage = styled.img`
    width: 100%;
    height: auto;
`;

const PrevButton = styled.button`
    position: absolute;
    left: 10px;
    top: 50%;
    transform: translateY(-50%);
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    padding: 10px;
    border: none;
    cursor: pointer;
    
    &:hover {
    background-color: rgba(0, 0, 0, 0.7);
    }
`;

const NextButton = styled.button`
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    padding: 10px;
    border: none;
    cursor: pointer;
    
    &:hover {
    background-color: rgba(0, 0, 0, 0.7);
    }
`;

const ThumbnailsContainer = styled.div`
    display: flex;
    justify-content: center;
    gap: 10px;
    position:relative;
    margin-top: 10px;
`;

const Thumbnail = styled.img`
    width: 20%;
    object-fit: cover;
    cursor: pointer;
    border: 2px solid transparent;
    transition: border-color 0.3s;
    border-color: ${(props) => (props.isActive ? 'var(--primary-color)' : 'transparent')};

    &:hover {
    border-color: #000;
    }
`;
    
function SlideImage({images,isButton = true}) {
    const [currentIndex, setCurrentIndex] = useState(0);
    

    const nextSlide = () => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    };

    const prevSlide = () => {
        setCurrentIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
    };

    const selectImage = (index) => {
        setCurrentIndex(index);
    };
    return (
        <SliderContainer>
          {/* Main image */}
          <MainImageContainer>
            <MainImage src={images[currentIndex].imageUrl} alt="Main" />
          </MainImageContainer>
    
          
    
          {/* Thumbnails */}
          <ThumbnailsContainer>
            {isButton && <PrevButton onClick={prevSlide}><IoIosArrowBack /></PrevButton>}
            {images.map((image, index) => (
              <Thumbnail onMouseEnter={() => selectImage(index)}
                key={index}
                src={image.imageUrl}
                alt={`Thumbnail ${index}`}
                isActive={currentIndex === index}
                onClick={() => selectImage(index)}
              />
            ))}
            {isButton && <NextButton onClick={nextSlide}><IoIosArrowForward /></NextButton>}
          </ThumbnailsContainer>
        </SliderContainer>
      );
};

    
    // Styled Components
    
    

export default SlideImage
