import { useEffect, useRef } from "react";

export function useCloseForm(action, listCapturing = true) {
  const ref = useRef();
  useEffect(
    function () {
      function handleClick(e) {
        if (ref.current && !ref.current.contains(e.target)) {
          console.log(ref.current, e.target)
          action();
        }
      }
      document.addEventListener("click", handleClick, listCapturing);
      return () => document.removeEventListener("click", handleClick);
    },
    [action, listCapturing]
  );
  return ref;
}
