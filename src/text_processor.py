import string
import collections

import nltk


class TextProcessor():
    def __init__(self, text, lang='french'):
        self.source_text = text
        self.lang = lang
        self.stopwords = nltk.corpus.stopwords.words(self.lang)
        self.sentences = nltk.tokenize.sent_tokenize(text, language=self.lang)
        self.vectors = dict(zip(self.sentences,
                                self.compute_vectors(self.sentences)))

    def compute_vectors(self, sentences):
        """Compute word vector for each sentence
        Returns a list of collections.Counter"""
        vectors = list()
        for sentence in sentences:
            words = nltk.word_tokenize(sentence, language=self.lang)
            words = self.filter_words(words)
            words = collections.Counter(words)
            vectors.append(words)
        return vectors

    def filter_words(self, words):
        return filter(
            lambda x: x not in self.stopwords and x not in string.punctuation,
            words)


if __name__ == '__main__':
    import sys
    if len(sys.argv) < 2:
        print("Usage: {0} sentence".format(sys.argv[0]))
        sys.exit(1)

    lang = 'french'
    if len(sys.argv) == 3:
        lang = sys.argv[2]
    d = TextProcessor(sys.argv[1], lang)
    print(d.vectors)
