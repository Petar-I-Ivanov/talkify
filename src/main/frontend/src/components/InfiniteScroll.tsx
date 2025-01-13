import React, { useEffect, useRef } from "react";
import { useInView } from "react-intersection-observer";
import type { SWRInfiniteResponse } from "swr/infinite";

type Props<T> = {
  swr: SWRInfiniteResponse<T>;
  children: (item: T, index?: number) => React.ReactNode;
  loadingIndicator?: React.ReactNode;
  emptyIndicator?: React.ReactNode;
  isAll: (swr: SWRInfiniteResponse<T>, size: number) => boolean;
  isEmpty: (swr: SWRInfiniteResponse<T>) => boolean;
  offset?: number;
};

const InfiniteScroll = <T,>(props: Props<T>): React.ReactElement<Props<T>> => {
  const {
    swr,
    swr: { size, setSize, data, isLoading },
    children,
    loadingIndicator,
    emptyIndicator,
    isAll,
    isEmpty,
    offset = 0,
  } = props;

  const hasLoadedRef = useRef(false);
  const { ref, inView } = useInView({ threshold: 0.1 });

  useEffect(() => {
    if (inView && !hasLoadedRef.current) {
      hasLoadedRef.current = true;
      setSize(size + 1);
    } else if (!inView) {
      hasLoadedRef.current = false;
    }
  }, [inView, setSize, size]);

  return (
    <>
      {isEmpty(swr) ? (
        emptyIndicator
      ) : (
        <>
          {data?.map((item, index) => (
            <React.Fragment key={index}>{children(item, index)}</React.Fragment>
          ))}

          {isLoading && (
            <div style={{ position: "relative" }}>
              <div
                ref={ref}
                style={{ position: "absolute", top: offset }}
              ></div>
              {loadingIndicator}
            </div>
          )}

          {data && !isAll(swr, size) && (
            <div
              ref={ref}
              style={{ height: "1px", background: "transparent" }}
            />
          )}
        </>
      )}
    </>
  );
};

export default InfiniteScroll;
