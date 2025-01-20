import React, { createContext, useContext, useMemo, useState } from "react";

const SelectedChannelIdContext = createContext<
  | {
      channelId?: number;
      setChannelId: React.Dispatch<React.SetStateAction<number | undefined>>;
    }
  | undefined
>(undefined);

const SelectedChannelIdProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [channelId, setChannelId] = useState<number>();

  const context = useMemo(
    () => ({
      channelId,
      setChannelId,
    }),
    [channelId]
  );

  return useMemo(
    () => (
      <SelectedChannelIdContext.Provider value={context}>
        {children}
      </SelectedChannelIdContext.Provider>
    ),
    [context]
  );
};

export const useSelectedChannelId = () => {
  const context = useContext(SelectedChannelIdContext);
  if (!context) {
    throw new Error(
      "useSelectedChannelId must be used within a SelectedChannelIdProvider"
    );
  }
  return context;
};

export default SelectedChannelIdProvider;
