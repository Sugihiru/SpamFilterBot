import string
import collections
import re

import nltk


EMAIL_REGEX = re.compile(r'[^@]+@[^@]+\.[^@]+')


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
            words = [x.lower() for x in self.filter_words(words)]
            words = collections.Counter(words)
            vectors.append(words)
        return vectors

    def filter_words(self, words):
        return filter(
            lambda x: x not in self.stopwords and x not in string.punctuation,
            words)

    def compute_spam_score(self, sentence, vector):
        """Score goes between 0 and 100, 0 is spam, 100 is ham"""
        score = 100
        spam_words_malus = 30

        spam_words = ('pute', 'tg', 'gueule')
        for spam_word in spam_words:
            if spam_word in vector:
                score -= spam_words_malus

        # Check if sentence contains a link or an email
        # We do not need a very strict verification
        if 'http://' in sentence or 'https://' in sentence:
            return 100
        if EMAIL_REGEX.match(sentence):
            return 100

        # Bound score between 0 and 100
        if score <= 0:
            return 0
        elif score >= 100:
            return 100

        return score


if __name__ == '__main__':
    import sys
    if len(sys.argv) < 2:
        print("Usage: {0} sentence".format(sys.argv[0]))
        sys.exit(1)

    lang = 'french'
    if len(sys.argv) == 3:
        lang = sys.argv[2]
    d = TextProcessor(sys.argv[1], lang)
    for sentence, vector in d.vectors.items():
        print("{0}\n\t{1}\n\tScore: {2}".format(
            sentence, vector, d.compute_spam_score(sentence, vector)))
