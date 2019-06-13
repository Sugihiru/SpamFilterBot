import string
import collections

import nltk


STOPWORDS = nltk.corpus.stopwords.words("french") + ["j'ai"]
LANG = "french"


class TextProcessor():
    def __init__(self, text):
        self.source_text = text
        self.sentences = nltk.tokenize.sent_tokenize(text, language=LANG)
        self.vectors = dict(zip(self.sentences,
                                self.compute_vectors(self.sentences)))

    def compute_vectors(self, sentences):
        """Compute word vector for each sentence
        Returns a list of collections.Counter"""
        vectors = list()
        for sentence in sentences:
            words = nltk.word_tokenize(sentence, language=LANG)
            words = [x.lower() for x in self.filter_words(words)]
            words = collections.Counter(words)
            vectors.append(words)
        return vectors

    def filter_words(self, words):
        return filter(
            lambda x: x not in STOPWORDS and x not in string.punctuation,
            words)


if __name__ == '__main__':
    import sys
    if len(sys.argv) < 2:
        print("Usage: {0} sentence".format(sys.argv[0]))
        sys.exit(1)

    if len(sys.argv) == 3:
        lang = sys.argv[2]
    d = TextProcessor(sys.argv[1])
    for sentence, vector in d.vectors.items():
        print("{0}\n\t{1}\n\tScore: {2}".format(
            sentence, vector, d.compute_spam_score(sentence, vector)))
