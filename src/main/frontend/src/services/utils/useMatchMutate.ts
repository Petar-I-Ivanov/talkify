import { useSWRConfig } from "swr";
import MatchMutate from "~/models/common/MatchMutate";

const useMatchMutate = (): MatchMutate => {
  const { cache, mutate } = useSWRConfig();

  return (matcher: RegExp, ...args: any[]) => {
    if (!(cache instanceof Map)) {
      throw new Error(
        "matchMutate requires the cache provider to be a Map instance"
      );
    }

    const keys = [];
    for (const key of (cache as any).keys()) {
      if (matcher.test(key)) {
        keys.push(key);
      }
    }

    const mutations = keys.map((key) => mutate(key, ...args));
    return Promise.all(mutations);
  };
};

export default useMatchMutate;
