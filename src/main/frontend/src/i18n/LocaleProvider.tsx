import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
} from "react";
import { MessageFormatElement } from "react-intl";
import bg from "./compiled-lang/bg.json";
import en from "./compiled-lang/en.json";
import useMatchMutate from "~/services/utils/useMatchMutate";

export type SupportedLocale = "en" | "bg";

type LocaleContextType = {
  locale: SupportedLocale;
  changeLocale: (value: SupportedLocale) => void;
  supportedLocales: Record<string, { label: string }>;
  messages: Record<string, string> | Record<string, MessageFormatElement[]>;
};

const LANGUAGES = {
  en: { label: "English", messages: en },
  bg: { label: "Български", messages: bg },
};

const LANGUAGE_LOCAL_KEY = "language";

const supportedLanguages = Object.keys(LANGUAGES) as SupportedLocale[];

let chosenLanguage = (localStorage.getItem(LANGUAGE_LOCAL_KEY) ??
  navigator.languages
    .map((lang) => lang.split(/[-_]/)[0])
    .find((lang) =>
      supportedLanguages.forEach((suppLang) => suppLang.includes(lang))
    ) ??
  supportedLanguages[0]) as SupportedLocale;

const LocaleContext = createContext<LocaleContextType>({
  locale: chosenLanguage,
  changeLocale: () => {},
  supportedLocales: {},
  messages: {},
});

export const LocaleProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const matchMutate = useMatchMutate();
  const [locale, setLocale] = useState<SupportedLocale>(chosenLanguage);

  const changeLocale = useCallback(
    (lang: SupportedLocale) => {
      if (!supportedLanguages.includes(lang)) {
        console.warn(
          `Try to update locale to ${lang}. Supported locales are ${supportedLanguages}`
        );
        return;
      }

      setLocale((prev) => {
        if (prev != lang) {
          matchMutate(/\//);
          localStorage.setItem(LANGUAGE_LOCAL_KEY, lang);
          chosenLanguage = lang;
        }

        return lang;
      });
    },
    [matchMutate]
  );

  const provider = useMemo(
    () => ({
      locale,
      changeLocale,
      supportedLocales: LANGUAGES,
      messages: LANGUAGES[locale].messages,
    }),
    [locale, changeLocale]
  );

  return (
    <LocaleContext.Provider value={provider}>{children}</LocaleContext.Provider>
  );
};

export const getCurrentLocale = () => chosenLanguage;

const useLocale = () => {
  const context = useContext(LocaleContext);
  if (!context) {
    throw new Error("useLocale must be used within a LocaleProvider");
  }
  return context;
};

export default useLocale;
