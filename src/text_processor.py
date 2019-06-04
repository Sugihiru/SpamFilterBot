import collections

import nltk


class TextProcessor():
    def __init__(self, text):
        self.source_text = text
        self.sentences = nltk.tokenize.sent_tokenize(text, language='french')
        self.vectors = dict(zip(self.sentences,
                                self.compute_vectors(self.sentences)))

    def compute_vectors(self, sentences):
        """Compute word vector for each sentence
        Returns a list of collections.Counter"""
        vectors = list()
        for sentence in sentences:
            words = nltk.word_tokenize(sentence, language='french')
            words = collections.Counter(words)
            vectors.append(words)
        return vectors


if __name__ == '__main__':
    import sys
    if len(sys.argv) != 2:
        print("Usage: {0} sentence".format(sys.argv[0]))
        sys.exit(1)
    d = TextProcessor(sys.argv[1])
    print(d.vectors)
